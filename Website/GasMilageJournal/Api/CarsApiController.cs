using GasMilageJournal.Models;
using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;

namespace GasMilageJournal.Api
{
    [Route("/api/Cars")]
    [Authorize("Authentication")]
    public class CarsApiController : BaseApiController<Car, ICarService, CarsApiController>
    {
        public CarsApiController(ICarService carService, ILoggerFactory loggerFactory)
            : base (carService, loggerFactory)
        {
        }
    }
}
