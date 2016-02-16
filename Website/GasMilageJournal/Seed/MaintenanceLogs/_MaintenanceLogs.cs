using GasMilageJournal.Models;
using System.Collections.Generic;

namespace GasMilageJournal.Seed
{
    public static partial class SeedData
    {
        public static List<MaintenanceLog> MaintenanceLogs = new List<MaintenanceLog>
        {
            RaptorMaintenanceLog1,
            RaptorMaintenanceLog2
        };
    }
}
