using System.Linq;
using System;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using Microsoft.AspNet.Identity.EntityFramework;
using GasMileageJournal.Models.Users;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.FillUps;
using GasMileageJournal.Models.MaintenanceLogs;

namespace GasMileageJournal.Models.Data
{
    public class DataContext : DbContext
    {
#if DEBUG
        private static String _connectionStringName = "Debug";
#else
        private static String _connectionStringName = "Release";
#endif

        public static String ConnectionStringName
        {
            get { return _connectionStringName; }
            set { _connectionStringName = value; }
        }

        public DataContext()
            : base(ConnectionStringName)
        {
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            #region Asp.Net Identity Naming

            modelBuilder.Entity<IdentityUser>().ToTable("gas_mileage_journal_users");
            modelBuilder.Entity<IdentityUser>().Property(t => t.Id).HasColumnName("id");
            modelBuilder.Entity<IdentityUser>().Property(t => t.Email).HasColumnName("email");
            modelBuilder.Entity<IdentityUser>().Property(t => t.EmailConfirmed).HasColumnName("email_confirmed");
            modelBuilder.Entity<IdentityUser>().Property(t => t.PasswordHash).HasColumnName("password_hash");
            modelBuilder.Entity<IdentityUser>().Property(t => t.SecurityStamp).HasColumnName("security_stamp");
            modelBuilder.Entity<IdentityUser>().Property(t => t.PhoneNumber).HasColumnName("phone_number");
            modelBuilder.Entity<IdentityUser>().Property(t => t.PhoneNumberConfirmed).HasColumnName("phone_number_confirmed");
            modelBuilder.Entity<IdentityUser>().Property(t => t.TwoFactorEnabled).HasColumnName("two_factor_enabled");
            modelBuilder.Entity<IdentityUser>().Property(t => t.LockoutEndDateUtc).HasColumnName("lockout_end_date_utc");
            modelBuilder.Entity<IdentityUser>().Property(t => t.LockoutEnabled).HasColumnName("lockout_enabled");
            modelBuilder.Entity<IdentityUser>().Property(t => t.AccessFailedCount).HasColumnName("access_failed_count");
            modelBuilder.Entity<IdentityUser>().Property(t => t.UserName).HasColumnName("username");

            modelBuilder.Entity<User>().ToTable("gas_mileage_journal_users");

            modelBuilder.Entity<IdentityRole>().ToTable("gas_mileage_journal_roles");
            modelBuilder.Entity<IdentityRole>().Property(t => t.Id).HasColumnName("id");
            modelBuilder.Entity<IdentityRole>().Property(t => t.Name).HasColumnName("name");

            modelBuilder.Entity<IdentityUserRole>().ToTable("gas_mileage_journal_users_roles");
            modelBuilder.Entity<IdentityUserRole>().Property(t => t.UserId).HasColumnName("user_id");
            modelBuilder.Entity<IdentityUserRole>().Property(t => t.RoleId).HasColumnName("role_id");

            modelBuilder.Entity<IdentityUserLogin>().ToTable("gas_mileage_journal_users_logins");
            modelBuilder.Entity<IdentityUserLogin>().Property(t => t.LoginProvider).HasColumnName("login_provider");
            modelBuilder.Entity<IdentityUserLogin>().Property(t => t.ProviderKey).HasColumnName("provider_key");
            modelBuilder.Entity<IdentityUserLogin>().Property(t => t.UserId).HasColumnName("user_id");

            modelBuilder.Entity<IdentityUserClaim>().ToTable("gas_mileage_journal_users_claims");
            modelBuilder.Entity<IdentityUserClaim>().Property(t => t.Id).HasColumnName("id");
            modelBuilder.Entity<IdentityUserClaim>().Property(t => t.UserId).HasColumnName("user_id");
            modelBuilder.Entity<IdentityUserClaim>().Property(t => t.ClaimType).HasColumnName("claim_type");
            modelBuilder.Entity<IdentityUserClaim>().Property(t => t.ClaimValue).HasColumnName("claim_value");

            modelBuilder.Entity<IdentityUserLogin>().HasKey<string>(l => l.UserId);
            modelBuilder.Entity<IdentityRole>().HasKey<string>(r => r.Id);
            modelBuilder.Entity<IdentityUserRole>().HasKey(r => new { r.RoleId, r.UserId });

            #endregion

            //modelBuilder.Entity<Car>().HasRequired(t => t.User).WithMany(x => x.Cars);
            //modelBuilder.Entity<FillUp>().HasRequired(t => t.User).WithMany(x => x.FillUps);
            //modelBuilder.Entity<MaintenanceLog>().HasRequired(t => t.User).WithMany(x => x.MaintenanceLogs);
            //modelBuilder.Entity<FillUp>().HasRequired(t => t.Car).WithMany(x => x.FillUps);
            //modelBuilder.Entity<MaintenanceLog>().HasRequired(t => t.Car).WithMany(x => x.MaintenanceLogs);
            
            modelBuilder.Conventions.Remove<ManyToManyCascadeDeleteConvention>();
            modelBuilder.Conventions.Remove<OneToManyCascadeDeleteConvention>();
        }

        #region DbSet

        public DbSet<User> Users { get; set; }
        public DbSet<Car> Cars { get; set; }
        public DbSet<FillUp> FillUps { get; set; }
        public DbSet<MaintenanceLog> MaintenanceLogs { get; set; }

        #endregion

        public override int SaveChanges()
        {
            try {
                var changeSet = ChangeTracker.Entries<BaseModel>();

                if (changeSet != null) {
                    foreach (var entry in changeSet.Where(entry => entry.State != EntityState.Unchanged)) {
                        entry.Entity.UpdatedOn = DateTime.Now;
                    }
                }

                return base.SaveChanges();
            } catch (Exception) {
                return -1;
            }
        }

        public static DataContext Create()
        {
            return new DataContext();
        }
    }
}