using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJournal.Areas.Api.Models.FillUps;
using GasMileageJournal.Areas.Api.Models.MaintenanceLogs;
using GasMileageJournal.Models.MaintenanceLogs;
using GasMileageJournal.Models.Users;

namespace GasMileageJournal.Areas.Api.Controllers
{
    public class MaintenanceLogController : BaseApiController<MaintenanceLog>
    {
        [HttpGet]
        public List<MaintenanceLogTO> GetAll()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);
                var data = UserManager.Context.MaintenanceLogs.Where(t => t.UserId == user.Id).ToList();
                var results = new List<MaintenanceLogTO>();
                foreach (var result in data) {
                    results.Add(MaintenanceLogTO.FromData(result));
                }

                return results;
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}
