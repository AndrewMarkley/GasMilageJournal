using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using GasMileageJournal.Models.FillUps;
using GasMileageJournal.Models.MaintenanceLogs;
using GasMileageJournal.Models.Users;

namespace GasMileageJournal.Models.Cars
{
    [Table("gmj_cars")]
    public class Car : BaseModel
    {
        [ForeignKey("User")]
        [Column("user_id")]
        public String UserId { get; set; }
        public virtual User User { get; set; }

        [Column("year")]
        public Int32 Year { get; set; }

        [Column("name")]
        public String Name { get; set; }

        [Column("make")]
        public String Make { get; set; }

        [Column("model")]
        public String Model { get; set; }

        [Column("mileage")]
        public Decimal Mileage { get; set; }

        public virtual List<MaintenanceLog> MaintenanceLogs { get; set; }
        public virtual List<FillUp> FillUps { get; set; }
    }
}