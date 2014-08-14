using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GasMileageJournal.Controllers;

namespace GasMilageJournal.Controllers
{
    public class FillUpController : BaseController
    {
        // GET: FillUp
        public ActionResult Index()
        {
            return View();
        }
    }
}