using GasMilageJournal.Services.Interfaces;
using Microsoft.AspNet.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Threading.Tasks;

namespace GasMilageJournal.Api
{
    public class BaseController<T, Service, Controller> where Service : IBaseService<T>
    {
        public readonly Service _service;
        public readonly ILogger _logger;

        public BaseController(Service service, ILoggerFactory loggerFactory)
        {
            _service = service;
            _logger = loggerFactory.CreateLogger<Controller>();
        }

        [HttpGet]
        [Route("{id}")]
        public virtual async Task<ActionResult> Get(Guid id)
        {
            try {
                var result = await (_service).GetByIdAsync(id);

                return result;
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        [HttpGet]
        public async Task<ActionResult> GetAll()
        {
            try {
                var result = await _service.GetAllAsync();

                return result;
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        [HttpPost]
        [Route("{data}")]
        public async Task<ActionResult> Save(T data)
        {
            try {
                var result = await _service.SaveAsync(data);

                return result;
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        [HttpDelete]
        [Route("{id}")]
        public async Task<ActionResult> Save(Guid id)
        {
            try {
                var result = await _service.DeleteAsync(id);

                return result;
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }
    }
}
