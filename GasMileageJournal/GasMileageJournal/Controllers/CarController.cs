using System.Web.Mvc;

namespace GasMileageJournal.Controllers
{
    [Authorize]
    public class CarController : BaseController
    {
        // GET: Car
        public ActionResult Index()
        {
            return View();
        }
    }
}