using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Users;

namespace GasMileageJournal.Models.FillUps
{
    [Table("gmj_fill_ups")]
    public class FillUp : BaseModel
    {
        [ForeignKey("User")]
        [Column("user_id")]
        public String UserId { get; set; }
        public virtual User User { get; set; }

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
        public Decimal Comments { get; set; }

        [Column("date")]
        public DateTime Date { get; set; }

        [Column("receipt")]
        public Byte[] Receipt { get; set; }
    }
}
