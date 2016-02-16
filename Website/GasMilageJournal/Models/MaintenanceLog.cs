using System;
using System.Collections.Generic;

namespace GasMilageJournal.Models
{
    public class MaintenanceLog : BaseModel<MaintenanceLog>
    {
        public Guid CarId { get; set; }
        public virtual Car Car { get; set; }

        public decimal Cost { get; set; }

        public decimal Odometer { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public string Location { get; set; }

        public virtual ICollection<Document> Documents { get; set; } = new HashSet<Document>();
    }
}
