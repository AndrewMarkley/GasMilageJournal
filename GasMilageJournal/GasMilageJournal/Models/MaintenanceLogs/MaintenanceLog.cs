using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Users;

namespace GasMileageJournal.Models.MaintenanceLogs
{
    [Table("gmj_maintenance_logs")]
    public class MaintenanceLog : BaseModel
    {
        [ForeignKey("User")]
        [Column("user_id")]
        public String UserId { get; set; }
        public virtual User User { get; set; }

        [ForeignKey("Car")]
        [Column("car_id")]
        public String CarId { get; set; }
        public virtual Car Car { get; set; }

        [Column("odometer")]
        public Decimal Odometer { get; set; }

        [Column("title")]
        public Decimal Title { get; set; }

        [Column("description")]
        public Decimal Description { get; set; }

        [Column("total_cost")]
        public Decimal TotalCost { get; set; }

        [Column("location")]
        public Decimal Location { get; set; }

        [Column("comments")]
        public Decimal Comments { get; set; }

        [Column("date")]
        public DateTime Date { get; set; }

        [Column("receipt")]
        public Byte[] Receipt { get; set; }
    }
}