using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJournal.Areas.Api.Models.FillUps;
using GasMileageJournal.Models.FillUps;
using GasMileageJournal.Models.Users;

namespace GasMileageJournal.Areas.Api.Controllers
{
    public class FillUpController : BaseApiController<FillUp>
    {
        [HttpGet]
        public List<FillUpTO> GetAll()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);
                var data = UserManager.Context.FillUps.Where(t => t.UserId == user.Id).ToList();

                return data.Select(FillUpTO.FromData).ToList();
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}