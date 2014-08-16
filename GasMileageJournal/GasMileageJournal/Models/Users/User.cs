using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Security.Claims;
using System.Threading.Tasks;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Data;
using GasMileageJournal.Models.FillUps;
using GasMileageJournal.Models.MaintenanceLogs;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;

namespace GasMileageJournal.Models.Users
{
    [Table("gmj_users")]
    public class User : IdentityUser, IDataModel
    {
        public User()
        {
            CreatedOn = DateTime.Now;
            UpdatedOn = DateTime.Now;

            ApiKey = DataUtility.GetId();

        }

        public String UserId { get; set; }

        [Required]
        [Column("created_on")]
        [DataType(DataType.DateTime)]
        [DisplayFormat(DataFormatString = "{0:dd MMM yyyy}")]
        public DateTime CreatedOn { get; set; }

        [Required]
        [Column("updated_on")]
        [DataType(DataType.DateTime)]
        [DisplayFormat(DataFormatString = "{0:dd MMM yyyy}")]
        public DateTime UpdatedOn { get; set; }

        [Column("api_key")]
        [StringLength(36)]
        public String ApiKey { get; set; }

        public List<Car> Cars { get; set; }
        public List<FillUp> FillUps { get; set; }
        public List<MaintenanceLog> MaintenanceLogs { get; set; }

        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager<User> manager)
        {
            // Note the authenticationType must match the one defined in CookieAuthenticationOptions.AuthenticationType
            var userIdentity =
                await manager.CreateIdentityAsync(this, DefaultAuthenticationTypes.ApplicationCookie);

            return userIdentity;
        }

        public static void Seed(DataContext context)
        {
            var users = new List<User>
            {
                new User {
                    Email = "Email"
                }
            };

            context.Users.AddRange(users);
            context.SaveChanges();
        }
    }
}