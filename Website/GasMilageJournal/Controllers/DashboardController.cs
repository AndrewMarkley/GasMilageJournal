using GasMilageJournal.Models;
using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using System.Threading.Tasks;

namespace GasMilageJournal.Controllers
{
    [Authorize("Authentication")]
    public class DashboardController : Controller
    {
        private readonly DataContext _dataContext;
        private readonly ICarService _carService;
        private readonly IFillUpService _fillUpService;
        private readonly IMaintenanceLogService _maintenanceLogService;

        public DashboardController(DataContext dataContext, ICarService carService, IFillUpService fillUpService, IMaintenanceLogService maintenanceLogService)
        {
            _dataContext = dataContext;
            _carService = carService;
            _fillUpService = fillUpService;
            _maintenanceLogService = maintenanceLogService;
        }

        public async Task<IActionResult> Index()
        {
            ViewBag.Cars = await _carService.GetAllAsync();
            ViewBag.FillUps = await _fillUpService.GetAllAsync();
            ViewBag.MaintenanceLogs = await _maintenanceLogService.GetAllAsync();

            return View();
        }
    }
}
