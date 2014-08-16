using System.Web.Mvc;

namespace GasMileageJournal.Controllers
{
    [Authorize]
    public class MaintenanceLogController : BaseController
    {
        // GET: MaintenanceLog
        public ActionResult Index()
        {
            return View();
        }
    }
}