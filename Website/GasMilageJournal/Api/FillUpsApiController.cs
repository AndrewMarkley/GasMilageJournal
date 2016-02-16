using GasMilageJournal.Models;
using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Authorization;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;

namespace GasMilageJournal.Api
{
    [Route("/api/FillUps")]
    [Authorize("Authentication")]
    public class FillUpsController : BaseApiController<FillUp, IFillUpService, FillUpsController>
    {
        public FillUpsController(IFillUpService service, ILoggerFactory loggerFactory)
            : base (service, loggerFactory)
        {
        }
    }
}
