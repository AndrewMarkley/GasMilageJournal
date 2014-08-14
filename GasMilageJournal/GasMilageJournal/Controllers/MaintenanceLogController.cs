using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GasMileageJournal.Controllers;

namespace GasMilageJournal.Controllers
{
    public class MaintenanceLogController : BaseController
    {
        // GET: MaintenanceLog
        public ActionResult Index()
        {
            return View();
        }
    }
}