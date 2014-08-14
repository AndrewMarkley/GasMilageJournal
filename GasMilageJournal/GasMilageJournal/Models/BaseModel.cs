using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using GasMileageJournal.Models.Data;

namespace GasMileageJournal.Models
{
    public class BaseModel : IDataModel
    {
        protected BaseModel()
        {
            Id = DataUtility.GetId();
            CreatedOn = DateTime.Now;
            UpdatedOn = DateTime.Now;
        }

        [Key]
        [StringLength(36)]
        [Column("id", Order = 0)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public String Id { get; set; }

        [Required]
        [Column("created_on", TypeName = "DateTime2", Order = 1)]
        public DateTime CreatedOn { get; set; }

        [Column("updated_on", TypeName = "DateTime2", Order = 2)]
        public DateTime UpdatedOn { get; set; }
    }
}