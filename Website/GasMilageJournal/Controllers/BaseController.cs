using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Threading.Tasks;

namespace GasMilageJournal.Api
{
    public class BaseController<T, Service, U> : Controller where Service : IBaseService<T>
    {
        public readonly Service _service;
        public readonly ILogger _logger;

        public BaseController(Service service, ILoggerFactory loggerFactory)
        {
            _service = service;
            _logger = loggerFactory.CreateLogger<U>();
        }

        public virtual async Task PopulateEditViewBag(T data)
        {
        }

        [HttpGet]
        public async Task<IActionResult> Index()
        {
            var result = await _service.GetAllAsync();

            return View(result);
        }

        [Route("{id}")]
        public virtual async Task<IActionResult> Edit(string id)
        {
            if (id == null) {
                return RedirectToAction("Index");
            }

            var result = await _service.Edit(id);

            if (!result.HasError) {
                await PopulateEditViewBag((T)result.Value);
            }

            return View(result);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public virtual async Task<ActionResult> Index(T data)
        {
            if (ModelState.IsValid) {
                var result = await _service.SaveAsync(data);

                if (result.HasError) {
                    foreach (var message in result.Messages) {
                        ModelState.AddModelError("", message);
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

            await _service.DeleteAsync(id);

            return RedirectToAction("Index");
        }
    }
}
