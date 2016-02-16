using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;
using GasMilageJournal.Services.Interfaces;
using GasMilageJournal.Models;
using GasMilageJournal.Api;

namespace GasMilageJournal.Controllers
{
    [Authorize("Authentication")]
    [Route("FillUps")]
    public class FillUpsController : BaseController<FillUp, IFillUpService, FillUpsController>
    {
        public FillUpsController(IFillUpService fillUpService, ILoggerFactory loggerFactory)
            : base(fillUpService, loggerFactory)
        {
        }
    }
}
