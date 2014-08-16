using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Web;
using GasMileageJournal.Models.Users;
using Microsoft.AspNet.Identity;

namespace GasMileageJournal.Models.Data
{
    public class BaseModel : IDataModel
    {
        protected BaseModel()
        {
            Id = DataUtility.GetId();
            CreatedOn = DateTime.Now;
            UpdatedOn = DateTime.Now;

            if (HttpContext.Current != null && HttpContext.Current.User != null &&
                HttpContext.Current.User.Identity != null &&
                !String.IsNullOrEmpty(HttpContext.Current.User.Identity.GetUserId())) {
                UserId = HttpContext.Current.User.Identity.GetUserId();
            }
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

        [ForeignKey("User")]
        [Column("user", Order = 4)]
        public String UserId { get; set; }
        public virtual User User { get; set; }
    }
}