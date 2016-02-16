using System;
using System.Collections.Generic;

namespace GasMilageJournal.Models
{
    public class Document : BaseModel<Document>
    {
        public Guid? FillUpId { get; set; }
        public virtual FillUp FillUp { get; set; }

        public Guid? MaintenanceLogId { get; set; }
        public virtual MaintenanceLog MaintenanceLog { get; set; }

        public Guid? CarId { get; set; }
        public virtual Car Car { get; set; }

        public string Extension { get; set; }
    }
}
