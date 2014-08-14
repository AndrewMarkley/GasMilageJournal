using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GasMileageJournal.Models.Users;
using Microsoft.AspNet.Identity;

namespace GasMileageJournal.Controllers
{
    [Authorize]
    public class BaseController : Controller
    {
        public String CurrentUserId
        {
            get { return User.Identity.GetUserId(); }
        }

        public String CurrentUserName
        {
            get { return User.Identity.GetUserName(); }
        }

        protected override void OnActionExecuting(ActionExecutingContext actionExecutingContext)
        {
#if DEBUG
            ViewBag.Release = false;
#else
            ViewBag.Release = true;
#endif
            base.OnActionExecuting(actionExecutingContext);
        }
    }
}