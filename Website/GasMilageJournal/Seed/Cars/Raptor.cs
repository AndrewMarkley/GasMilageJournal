using GasMilageJournal.Models;
using System;

namespace GasMilageJournal.Seed
{
    public static partial class SeedData
    {
        public static Car Raptor => new Car
        {
            Id = new Guid("7d75b459-64af-4cb2-8206-d6840784f235"),
            Make = "Ford",
            Milage = 21369,
            Model = "Raptor",
            Name = "Raptor",
            UserId = HenryFord.Id,
            Year = 2015
        };
    }
}
