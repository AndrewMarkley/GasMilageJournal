using GasMilageJournal.Models.Interfaces;
using Newtonsoft.Json;
using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GasMilageJournal.Models
{
    public abstract class BaseModel<T> : IBaseModel where T : IBaseModel
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public virtual Guid Id { get; set; }

        public string UserId { get; set; }
        [JsonIgnore]
        public virtual User User { get; set; }

        [DisplayFormat(DataFormatString = "{0:yyyy/MM/dd hh:mm tt}", ApplyFormatInEditMode = true)]
        public DateTime Created { get; set; }

        public virtual void Validate()
        {

        }
    }
}
