using GasMilageJournal.Models;
using Microsoft.Data.Entity;
using System;

namespace GasMilageJournal.Seed
{
    public static class SeedManager
    {
        public static void Seed()
        {
            using (var dataContext = DataContext.GetInstance()) {
                try {
                    dataContext.Database.EnsureDeletedAsync().Wait();
                    dataContext.Database.EnsureCreatedAsync().Wait();

                    dataContext.ChangeTracker.AutoDetectChangesEnabled = false;

                    dataContext.Database.ExecuteSqlCommand("EXEC sp_MSforeachtable @command1=\"ALTER TABLE ? NOCHECK CONSTRAINT ALL\"");

                    dataContext.Cars.AddRange(SeedData.Cars);
                    dataContext.FillUps.AddRange(SeedData.FillUps);
                    dataContext.MaintenanceLogs.AddRange(SeedData.MaintenanceLogs);
                    dataContext.Users.AddRange(SeedData.Users);

                    dataContext.SaveChanges();

                    dataContext.Database.ExecuteSqlCommand("EXEC sp_MSforeachtable @command1=\"ALTER TABLE ? CHECK CONSTRAINT ALL\"");
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }
}
