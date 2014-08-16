using System;
using System.ComponentModel.DataAnnotations.Schema;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Data;

namespace GasMileageJournal.Models.FillUps
{
    [Table("gmj_fill_ups")]
    public class FillUp : BaseModel
    {
        [ForeignKey("Car")]
        [Column("car_id")]
        public String CarId { get; set; }
        public virtual Car Car { get; set; }

        [Column("distance")]
        public Decimal Distance { get; set; }

        [Column("gas")]
        public Decimal Gas { get; set; }

        [Column("price")]
        public Decimal Price { get; set; }

        [Column("total_cost")]
        public Decimal TotalCost { get; set; }

        [Column("mpg")]
        public Decimal Mpg { get; set; }

        [Column("comments")]
        public String Comments { get; set; }

        [Column("fill_up_date")]
        public DateTime? FillUpDate { get; set; }

        [Column("receipt")]
        public Byte[] Receipt { get; set; }
    }
}
