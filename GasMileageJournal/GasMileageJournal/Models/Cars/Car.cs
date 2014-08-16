using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using GasMileageJournal.Models.Data;
using GasMileageJournal.Models.FillUps;
using GasMileageJournal.Models.MaintenanceLogs;

namespace GasMileageJournal.Models.Cars
{
    [Table("gmj_cars")]
    public class Car : BaseModel
    {
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