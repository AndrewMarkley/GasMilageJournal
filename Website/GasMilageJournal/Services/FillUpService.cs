using GasMilageJournal.Services.Interfaces;
using System;
using System.Linq;
using System.Threading.Tasks;
using GasMilageJournal.Models;
using Microsoft.Data.Entity;
using GasMilageJournal.Extensions;
namespace GasMilageJournal.Services
{
    public class FillUpService : IFillUpService
    {
        private DataContext _dataContext;
        private IApplicationService _appService;

        public FillUpService(DataContext dataContext, IApplicationService appService)
        {
            _dataContext = dataContext;
            _appService = appService;
        }

        public async Task<ServiceResult> DeleteAsync(Guid id)
        {
            try {
                var fillUp = await _dataContext.FillUps.Where(t => t.Id == id)
                                                       .SingleOrDefaultAsync();

                await DeleteAsync(fillUp);

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> DeleteAsync(FillUp fillUp)
        {
            try {
                _dataContext.FillUps.Remove(fillUp);
                await _dataContext.SaveChangesAsync();

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> Edit(string id)
        {
            try {
                FillUp fillUp;

                if (id.ToLower().Equals("new")) {
                    fillUp = new FillUp
                    {
                        Created = DateTime.Now,
                        UserId = _appService.UserId
                    };
                } else {
                    fillUp = await _dataContext.FillUps.Where(t => t.UserId == _appService.UserId)
                                                       .Where(t => t.Id == id.ToGuid())
                                                       .SingleAsync();
                }

                return new ServiceResult(fillUp);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetAllAsync()
        {
            try {
                var fillUps = await _dataContext.FillUps.ToListAsync();

                return new ServiceResult(fillUps);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetByIdAsync(Guid id)
        {
            try {
                var fillUp = await _dataContext.FillUps.Where(t => t.Id == id)
                                                       .SingleOrDefaultAsync();

                return new ServiceResult(fillUp);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> SaveAsync(FillUp fillUp)
        {
            try {
                await _dataContext.AddOrUpdateAsync(fillUp);
                _dataContext.SaveChanges();

                return new ServiceResult(fillUp);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }
    }
}
