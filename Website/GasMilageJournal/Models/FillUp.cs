using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace GasMilageJournal.Models
{
    public class FillUp : BaseModel<FillUp>
    {
        public decimal Distance { get; set; }

        public decimal Gas { get; set; }

        public decimal Price { get; set; }

        [NotMapped]
        public decimal TotalCost => Gas * Price;

        [NotMapped]
        public decimal MPG => Distance / Gas;

        public string Comments { get; set; }

        public Guid CarId { get; set; }
        public virtual Car Car { get; set; }

        public virtual ICollection<Document> Documents { get; set; } = new HashSet<Document>();
    }
}
