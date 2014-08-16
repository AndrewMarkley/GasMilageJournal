using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using GasMileageJournal.Models.FillUps;

namespace GasMileageJournal.Controllers
{
    [Authorize]
    public class HomeController : Controller
    {
        // GET: Home
        public ActionResult Index()
        {
            return View();
        }

        // GET: Home
        [HttpGet]
        public JsonResult GetFillUpData()
        {
            using (var fillUpManager = new FillUpManager()) {
                var fillUps = fillUpManager.Context.FillUps.ToList();

                var data = new List<object> {new[] {"Date", "Cost", "Gallons"}};
                data.AddRange(fillUps.Select(fillUp => new[] {fillUp.CreatedOn.ToShortDateString(), fillUp.TotalCost.ToString(), fillUp.Gas.ToString()}));

                return Json(data, JsonRequestBehavior.AllowGet);
            }
        }
    }
}
