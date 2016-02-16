using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;
using GasMilageJournal.Services.Interfaces;
using GasMilageJournal.Models;
using GasMilageJournal.Api;

namespace GasMilageJournal.Controllers
{
    [Authorize("Authentication")]
    [Route("MaintenanceLogs")]
    public class MaintenanceLogsController : BaseController<MaintenanceLog, IMaintenanceLogService, MaintenanceLogsController>
    {
        public MaintenanceLogsController(IMaintenanceLogService maintenanceLogService, ILoggerFactory loggerFactory)
            : base(maintenanceLogService, loggerFactory)
        {
        }
    }
}
