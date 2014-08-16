using System.Web.Mvc;

namespace GasMileageJournal.Controllers
{
    [Authorize]
    public class FillUpController : BaseController
    {
        // GET: FillUp
        public ActionResult Index()
        {
            return View();
        }
    }
}