using GasMilageJournal.Models.Interfaces;
using Microsoft.AspNet.Identity.EntityFramework;
using System;
using System.Collections.Generic;

namespace GasMilageJournal.Models
{
    public partial class User : IdentityUser, IBaseModel<string>
    {
        public DateTime Created { get; set; }
        public virtual ICollection<Car> Cars { get; set; } = new HashSet<Car>();
        public virtual ICollection<FillUp> FillUps { get; set; } = new HashSet<FillUp>();
        public virtual ICollection<MaintenanceLog> MaintenanceLogs { get; set; } = new HashSet<MaintenanceLog>();
    }
}
