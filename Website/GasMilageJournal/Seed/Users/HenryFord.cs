using GasMilageJournal.Models;
using System;

namespace GasMilageJournal.Seed
{
    public static partial class SeedData
    {
        public static User HenryFord => new User
        {
            Id = "49e3c8e3-1e13-4539-aa1f-c8f9b045e065",
            Email = "HenryFord@ford.com",
            Created = DateTime.Now,
            UserName = "HenryFord@ford.com",
            ConcurrencyStamp = new Guid("4c4abe32-86f0-4d9d-a574-da703cb3fe94").ToString(),
            LockoutEnabled = true,
            LockoutEnd = null,
            AccessFailedCount = 0,
            NormalizedEmail = "HENRYFORD@FORD.COM",
            NormalizedUserName = "HENRYFORD@FORD.COM",
            EmailConfirmed = false,
            PasswordHash = SeedConstants.PasswordHash,
            SecurityStamp = new Guid("6b17272f-d2bb-4bda-8a1b-f6fd973fbe19").ToString(),
            TwoFactorEnabled = false,
            PhoneNumber = "555-555-5555",
            PhoneNumberConfirmed = false,
        };
    }
}
