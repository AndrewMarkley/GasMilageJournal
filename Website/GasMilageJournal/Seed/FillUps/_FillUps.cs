using GasMilageJournal.Models;
using System.Collections.Generic;

namespace GasMilageJournal.Seed
{
    public static partial class SeedData
    {
        public static List<FillUp> FillUps = new List<FillUp>
        {
            RaptorFillUp1,
            RaptorFillUp2,
            RaptorFillUp3,
            RaptorFillUp4,
            RaptorFillUp5
        };
    }
}
