using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJounal.Areas.Api.Controllers;
using GasMileageJournal.Models.MaintenanceLogs;
using GasMileageJournal.Models.Users;

namespace GasMilageJournal.Areas.Api.Controllers
{
    public class MaintenanceLogController : BaseApiController
    {
        [HttpGet]
        public List<MaintenanceLog> GetMaintenanceLogs()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);
                var maintenanceLogs = UserManager.Context.MaintenanceLogs.Where(t => t.UserId == user.Id).ToList();

                return maintenanceLogs;
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }

        [HttpGet]
        public String Save()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);

                var json = GetHeader("MaintenanceLog");
                var maintenanceLog = Deserialize<MaintenanceLog>(json);

                using (var maintenanceLogManager = new MaintenanceLogManager()) {
                    maintenanceLog.UserId = user.Id;
                    return maintenanceLogManager.Save(maintenanceLog);
                }
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}
