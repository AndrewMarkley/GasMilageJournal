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
    [Route("Cars")]
    public class CarsController : Controller
    {
        private readonly ICarService _carService;
        private readonly ILogger _logger;

        public CarsController(ICarService carService, ILoggerFactory loggerFactory)
        {
            _carService = carService;
            _logger = loggerFactory.CreateLogger<CarsController>();
        }

        private async Task PopulateEditViewBag(Car car)
        {
        }

        [HttpGet]
        public async Task<IActionResult> Index()
        {
            var result = await _carService.GetAllAsync();

            return View(result.Result);
        }

        [Route("{id}")]
        public virtual async Task<IActionResult> Edit(string id)
        {
            if (id == null) {
                return RedirectToAction("Index");
            }

            var result = await _carService.Edit(id);

            await PopulateEditViewBag(result.Result);

            return View(result.Result);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public virtual async Task<ActionResult> Index(Car data)
        {
            if (ModelState.IsValid) {
                var result = await _carService.SaveAsync(data);

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
        public virtual async Task<ActionResult> Delete(Guid id)
        {
            if (id == null) {
                return RedirectToAction("Index");
            }

            await _carService.DeleteAsync(id);

            return RedirectToAction("Index");
        }
    }
}
