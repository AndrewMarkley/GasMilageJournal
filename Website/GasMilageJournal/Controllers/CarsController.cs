using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;
using GasMilageJournal.Services.Interfaces;
using GasMilageJournal.Models;
using GasMilageJournal.Api;

namespace GasMilageJournal.Controllers
{
    [Authorize("Authentication")]
    [Route("Cars")]
    public class CarsController : BaseController<Car, ICarService, CarsController>
    {
        public CarsController(ICarService carService, ILoggerFactory loggerFactory)
            : base(carService, loggerFactory)
        {
        }
    }
}
