using System.Collections.Generic;

namespace GasMilageJournal.Models
{
    public class Car : BaseModel<Car>
    {
        public int Year { get; set; }

        public string Name { get; set; }

        public string Make { get; set; }

        public string Model { get; set; }

        public double Milage { get; set; }

        public virtual ICollection<Document> Documents { get; set; } = new HashSet<Document>();
        public virtual ICollection<FillUp> FillUps { get; set; } = new HashSet<FillUp>();
        public virtual ICollection<MaintenanceLog> MaintenanceLogs { get; set; } = new HashSet<MaintenanceLog>();
    }
}
