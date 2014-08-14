using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GasMileageJournal.Controllers;

namespace GasMilageJournal.Controllers
{
    public class CarController : BaseController
    {
        // GET: Car
        public ActionResult Index()
        {
            return View();
        }
    }
}