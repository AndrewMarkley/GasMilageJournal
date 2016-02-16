using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.Data.Entity;
using Microsoft.Data.Entity.Infrastructure;

namespace GasMilageJournal.Models
{
    public class DataContext : IdentityDbContext<User, IdentityRole, string>
    {
        private DbContextOptions options;

        public DataContext()
        {
        }

        public DataContext(DbContextOptions options)
            : base(options)
        {
        }

        public static DataContext GetInstance()
        {
            var optionsBuilder = new DbContextOptionsBuilder();

            optionsBuilder.UseSqlServer(Startup.ConnectionString);

            return new DataContext(optionsBuilder.Options);
        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            // Customize the ASP.NET Identity model and override the defaults if needed.
            // For example, you can rename the ASP.NET Identity table names and more.
            // Add your customizations after calling base.OnModelCreating(builder);

            builder.Entity<User>().ToTable("User", "Identity");

            builder.Entity<Document>()
                   .HasOne(c => c.Car)
                   .WithMany(t => t.Documents)
                   .HasForeignKey(t => t.CarId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict)
                   .IsRequired(false);

            builder.Entity<Document>()
                   .HasOne(c => c.MaintenanceLog)
                   .WithMany(t => t.Documents)
                   .HasForeignKey(t => t.MaintenanceLogId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict)
                   .IsRequired(false);

            builder.Entity<Document>()
                   .HasOne(c => c.FillUp)
                   .WithMany(t => t.Documents)
                   .HasForeignKey(t => t.FillUpId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict)
                   .IsRequired(false);

            builder.Entity<MaintenanceLog>()
                   .HasOne(c => c.Car)
                   .WithMany(t => t.MaintenanceLogs)
                   .HasForeignKey(t => t.CarId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict);

            builder.Entity<FillUp>()
                   .HasOne(c => c.Car)
                   .WithMany(t => t.FillUps)
                   .HasForeignKey(t => t.CarId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict);

            builder.Entity<Car>()
                   .HasOne(c => c.User)
                   .WithMany(t => t.Cars)
                   .HasForeignKey(t => t.UserId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict);

            builder.Entity<FillUp>()
                   .HasOne(c => c.User)
                   .WithMany(t => t.FillUps)
                   .HasForeignKey(t => t.UserId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict);

            builder.Entity<MaintenanceLog>()
                   .HasOne(c => c.User)
                   .WithMany(t => t.MaintenanceLogs)
                   .HasForeignKey(t => t.UserId)
                   .OnDelete(Microsoft.Data.Entity.Metadata.DeleteBehavior.Restrict);
        }

        public DbSet<FillUp> FillUps { get; set; }
        public DbSet<Car> Cars { get; set; }
        public DbSet<MaintenanceLog> MaintenanceLogs { get; set; }
        public DbSet<Document> Documents { get; set; }

    }
}
