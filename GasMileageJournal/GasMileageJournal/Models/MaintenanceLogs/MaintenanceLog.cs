using System;
using System.ComponentModel.DataAnnotations.Schema;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Data;

namespace GasMileageJournal.Models.MaintenanceLogs
{
    [Table("gmj_maintenance_logs")]
    public class MaintenanceLog : BaseModel
    {
        [ForeignKey("Car")]
        [Column("car_id")]
        public String CarId { get; set; }
        public virtual Car Car { get; set; }

        [Column("odometer")]
        public Decimal Odometer { get; set; }

        [Column("title")]
        public String Title { get; set; }

        [Column("description")]
        public String Description { get; set; }

        [Column("total_cost")]
        public Decimal TotalCost { get; set; }

        [Column("location")]
        public String Location { get; set; }

        [Column("comments")]
        public String Comments { get; set; }

        [Column("date")]
        public DateTime? Date { get; set; }

        [Column("receipt")]
        public Byte[] Receipt { get; set; }
    }
}