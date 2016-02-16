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
    [Route("FillUps")]
    public class FillUpsController : Controller
    {
        private readonly IFillUpService _fillUpService;
        private readonly ILogger _logger;

        public FillUpsController(IFillUpService fillUpService, ILoggerFactory loggerFactory)
        {
            _fillUpService = fillUpService;
            _logger = loggerFactory.CreateLogger<ManageController>();
        }

        private async Task PopulateEditViewBag(FillUp fillUp)
        {
        }

        [HttpGet]
        public async Task<IActionResult> Index()
        {
            var result = await _fillUpService.GetAllAsync();

            return View(result.Result);
        }

        [Route("{id}")]
        public virtual async Task<IActionResult> Edit(string id)
        {
            if (id == null) {
                return RedirectToAction("Index");
            }

            var result = await _fillUpService.Edit(id);

            await PopulateEditViewBag(result.Result);

            return View(result.Result);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public virtual async Task<ActionResult> Index(FillUp data)
        {
            if (ModelState.IsValid) {
                var result = await _fillUpService.SaveAsync(data);

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

            await _fillUpService.DeleteAsync(new Guid(id));

            return RedirectToAction("Index");
        }
    }
}
