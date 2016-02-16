using GasMilageJournal.Models;
using System;

namespace GasMilageJournal.Seed
{
    public static partial class SeedData
    {
        public static MaintenanceLog RaptorMaintenanceLog1 => new MaintenanceLog
        {
            Id = new Guid("d5b82285-bb2e-4171-82d0-3447d6070363"),
            CarId = Raptor.Id,
            Cost = 589.65m,
            Created = DateTime.Now,
            Description = "Got the brakes flushed and changed",
            Location = "Quicky Brakes",
            Odometer = 58468.6m,
            Title = "Changed Brakes",
            UserId = HenryFord.Id,
        };
        public static MaintenanceLog RaptorMaintenanceLog2 => new MaintenanceLog
        {
            Id = new Guid("f1d86fc5-3e1c-4464-98da-b86dba30178f"),
            CarId = Raptor.Id,
            Cost = 21.06m,
            Created = DateTime.Now,
            Description = "Got the oil changed",
            Location = "Quicky Lube",
            Odometer = 58468.6m,
            Title = "Changed Oil",
            UserId = HenryFord.Id,
        };
    }
}