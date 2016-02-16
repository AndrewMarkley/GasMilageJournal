using GasMilageJournal.Models;
using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;

namespace GasMilageJournal.Api
{
    [Route("/api/MaintenanceLogs")]
    [Authorize("Authentication")]
    public class MaintenanceLogsApiController : BaseController<MaintenanceLog, IMaintenanceLogService, MaintenanceLogsApiController>
    {
        public MaintenanceLogsApiController(IMaintenanceLogService carService, ILoggerFactory loggerFactory)
            : base (carService, loggerFactory)
        {
        }
    }
}
