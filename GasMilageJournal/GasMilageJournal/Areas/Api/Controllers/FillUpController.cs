using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJounal.Areas.Api.Controllers;
using GasMileageJournal.Models.FillUps;
using GasMileageJournal.Models.Users;

namespace GasMilageJournal.Areas.Api.Controllers
{
    public class FillUpController : BaseApiController
    {
        [HttpGet]
        public List<FillUp> GetFillUps()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);
                var cars = UserManager.Context.FillUps.Where(t => t.UserId == user.Id).ToList();

                return cars;
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

                var json = GetHeader("FillUp");
                var fillUp = Deserialize<FillUp>(json);

                using (var fillUpManager = new FillUpManager()) {
                    fillUp.UserId = user.Id;
                    return fillUpManager.Save(fillUp);
                }
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}