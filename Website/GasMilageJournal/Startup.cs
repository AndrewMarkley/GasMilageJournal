using Microsoft.AspNet.Builder;
using Microsoft.AspNet.Hosting;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.Data.Entity;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using GasMilageJournal.Models;
using GasMilageJournal.Services;
using Newtonsoft.Json;
using GasMilageJournal.Services.Interfaces;
using GasMilageJournal.Security;
using GasMilageJournal.Extensions;

namespace GasMilageJournal
{
    public class Startup
    {
        public static string ConnectionString { get; set; }

        public Startup(IHostingEnvironment env)
        {
            // Set up configuration sources.
            var builder = new ConfigurationBuilder()
                .AddJsonFile("appsettings.json")
                .AddJsonFile($"appsettings.{env.EnvironmentName}.json", optional: true);

            builder.AddEnvironmentVariables();
            Configuration = builder.Build();
        }

        public IConfigurationRoot Configuration { get; set; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            ConnectionString = Configuration["Data:ConnectionStrings:Sql"];

            // Add framework services.
            services.AddEntityFramework()
                .AddSqlServer()
                .AddDbContext<DataContext>(options => options.UseSqlServer(ConnectionString));

            services.AddIdentity<User, IdentityRole>()
                .AddEntityFrameworkStores<DataContext>()
                .AddDefaultTokenProviders();

            services.AddMvc().AddJsonOptions(t => t.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Ignore);

            services.AddAuthorization(option =>
            {
                option.AddPolicy("Authentication", p => p.AddRequirements(new AuthenticationRequirement()));
            });

            JsonConvert.DefaultSettings = () => new JsonSerializerSettings
            {
                ReferenceLoopHandling = ReferenceLoopHandling.Ignore
            };

            // Add application services.
            services.AddTransient<IEmailSender, AuthMessageSender>();
            services.AddTransient<ISmsSender, AuthMessageSender>();

            services.AddScoped<IFillUpService, FillUpService>();
            services.AddScoped<ICarService, CarService>();
            services.AddScoped<IMaintenanceLogService, MaintenanceLogService>();
            services.AddScoped<IApplicationService, ApplicationService>();

#if SEED
            Seed.SeedManager.Seed();
#endif
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
        {
            loggerFactory.AddConsole(Configuration.GetSection("Logging"));
            loggerFactory.AddDebug();

            if (env.IsDevelopment()) {
                app.UseBrowserLink();
                app.UseDeveloperExceptionPage();
                app.UseDatabaseErrorPage();
            } else {
                app.UseExceptionHandler("/Shared/Error");

                // For more details on creating database during deployment see http://go.microsoft.com/fwlink/?LinkID=615859
                try {
                    using (var serviceScope = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>()
                        .CreateScope()) {
                        serviceScope.ServiceProvider.GetService<DataContext>()
                             .Database.Migrate();
                    }
                } catch { }
            }

            app.UseIISPlatformHandler(options => options.AuthenticationDescriptions.Clear());

            app.UseStaticFiles();

            app.UseCustomIdentity();

            // To configure external authentication please see http://go.microsoft.com/fwlink/?LinkID=532715

            // Default Routes
            // Define regular routes using attributes in controllers
            app.UseMvc(routes =>
            {
                routes.MapRoute(name: "Default", template: "{controller=Dashboard}/{action=Index}/{id?}");
            });
        }

        // Entry point for the application.
        public static void Main(string[] args) => WebApplication.Run<Startup>(args);
    }
}

