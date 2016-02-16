using GasMilageJournal.Models;
using System;

namespace GasMilageJournal.Seed
{
    public static partial class SeedData
    {
        public static FillUp RaptorFillUp1 => new FillUp
        {
            CarId = Raptor.Id,
            Comments = "",
            Created = DateTime.Now,
            Distance = 468m,
            Gas = 36m,
            Id = new Guid("c655e351-4d52-481c-ad55-c3b67d232f4c"),
            Price = 1.89m,
            UserId = HenryFord.Id,
        };

        public static FillUp RaptorFillUp2 => new FillUp
        {
            CarId = Raptor.Id,
            Comments = "",
            Created = DateTime.Now,
            Distance = 446.26m,
            Gas = 33.5m,
            Id = new Guid("6941f542-e55c-410b-976d-a77bfc73bb12"),
            Price = 1.85m,
            UserId = HenryFord.Id,
        };

        public static FillUp RaptorFillUp3 => new FillUp
        {
            CarId = Raptor.Id,
            Comments = "",
            Created = DateTime.Now,
            Distance = 323.46m,
            Gas = 24.32m,
            Id = new Guid("7611f9a4-a14d-40d0-a0d0-1bc42332ec2c"),
            Price = 1.74m,
            UserId = HenryFord.Id,
        };

        public static FillUp RaptorFillUp4 => new FillUp
        {
            CarId = Raptor.Id,
            Comments = "",
            Created = DateTime.Now,
            Distance = 412m,
            Gas = 30.59m,
            Id = new Guid("e2348282-59a9-4241-a96f-6ab218ac4589"),
            Price = 1.77m,
            UserId = HenryFord.Id,
        };

        public static FillUp RaptorFillUp5 => new FillUp
        {
            CarId = Raptor.Id,
            Comments = "",
            Created = DateTime.Now,
            Distance = 398m,
            Gas = 29.69m,
            Id = new Guid("bec0b70d-6a11-4237-b44c-dc6df80a68a9"),
            Price = 1.81m,
            UserId = HenryFord.Id,
        };
    }
}
