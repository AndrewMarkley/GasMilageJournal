using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJournal.Areas.Api.Models.Cars;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Users;

namespace GasMileageJournal.Areas.Api.Controllers
{
    public class CarController : BaseApiController<Car>
    {
        [HttpGet]
        public List<CarTO> GetAll()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);
                var data = UserManager.Context.Cars.Where(t => t.UserId == user.Id).ToList();

                return data.Select(CarTO.FromData).ToList();
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}