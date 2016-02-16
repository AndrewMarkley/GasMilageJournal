using System.Threading.Tasks;
using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;
using GasMilageJournal.Services.Interfaces;
using GasMilageJournal.Models;
using System;

namespace GasMilageJournal.Controllers
{
    [Authorize("Authentication")]
    [Route("MaintenanceLogs")]
    public class MaintenanceLogsController : Controller
    {
        private readonly IMaintenanceLogService _maintenanceLogService;
        private readonly ILogger _logger;

        public MaintenanceLogsController(IMaintenanceLogService maintenanceLogService, ILoggerFactory loggerFactory)
        {
            _maintenanceLogService = maintenanceLogService;
            _logger = loggerFactory.CreateLogger<ManageController>();
        }

        private async Task PopulateEditViewBag(MaintenanceLog maintenanceLog)
        {
        }

        [HttpGet]
        public async Task<IActionResult> Index()
        {
            var result = await _maintenanceLogService.GetAllAsync();

            return View(result.Result);
        }

        [Route("{id}")]
        public virtual async Task<IActionResult> Edit(string id)
        {
            if (id == null) {
                return RedirectToAction("Index");
            }

            var result = await _maintenanceLogService.Edit(id);

            await PopulateEditViewBag(result.Result);

            return View(result.Result);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public virtual async Task<ActionResult> Index(MaintenanceLog data)
        {
            if (ModelState.IsValid) {
                var result = await _maintenanceLogService.SaveAsync(data);

                if (result.HasError) {
                    foreach (var message in result.Messages) {
                        ModelState.AddModelError("", message.Message);
                    }
                }

                return RedirectToAction("Index");
            }

            await PopulateEditViewBag(data);

            return View("Edit", data);
        }

        [HttpDelete("{id}")]
        public virtual async Task<ActionResult> Delete(string id)
        {
            if (id == null) {
                return RedirectToAction("Index");
            }

            await _maintenanceLogService.DeleteAsync(new Guid(id));

            return RedirectToAction("Index");
        }
    }
}
