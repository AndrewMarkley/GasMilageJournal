using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using GasMileageJounal.Areas.Api.Controllers;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.Users;
using GasMileageJournal.Models.Data;

namespace GasMilageJournal.Areas.Api.Controllers
{
    public class CarController : BaseApiController
    {
        [HttpGet]
        public List<Car> GetCars()
        {
            try {
                var apiKey = GetHeader("ApiKey");
                var user = UserManager.Context.Users.FirstOrDefault(t => t.ApiKey == apiKey);
                var cars = UserManager.Context.Cars.Where(t => t.UserId == user.Id).ToList();

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

                var json = GetHeader("Car");
                var car = Deserialize<Car>(json);

                using (var carManager = new CarManager()) {
                    car.UserId = user.Id;
                    return carManager.Save(car);
                }
            } catch (Exception) {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }
        }
    }
}