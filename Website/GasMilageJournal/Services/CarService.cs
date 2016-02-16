using GasMilageJournal.Services.Interfaces;
using System;
using System.Linq;
using System.Threading.Tasks;
using GasMilageJournal.Models;
using Microsoft.Data.Entity;
using GasMilageJournal.Extensions;

namespace GasMilageJournal.Services
{
    public class CarService : ICarService
    {
        private DataContext _dataContext;
        private IApplicationService _appService;

        public CarService(DataContext dataContext, IApplicationService appService)
        {
            _dataContext = dataContext;
            _appService = appService;
        }

        public async Task<ServiceResult> DeleteAsync(Guid id)
        {
            try {
                var car = await _dataContext.Cars.Where(t => t.Id == id)
                                                       .SingleOrDefaultAsync();

                await DeleteAsync(car);

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> DeleteAsync(Car car)
        {
            try {
                _dataContext.Cars.Remove(car);
                await _dataContext.SaveChangesAsync();

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> Edit(string id)
        {
            try {
                Car car;

                if (id.ToLower().Equals("new")) {
                    car = new Car
                    {
                        Created = DateTime.Now,
                        UserId = _appService.UserId
                    };
                } else {
                    car = await _dataContext.Cars.Where(t => t.UserId == _appService.UserId)
                                                       .Where(t => t.Id == id.ToGuid())
                                                       .SingleAsync();
                }

                return new ServiceResult(car);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetAllAsync()
        {
            try {
                var cars = await _dataContext.Cars.ToListAsync();

                return new ServiceResult(cars);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetByIdAsync(Guid id)
        {
            try {
                var car = await _dataContext.Cars.Where(t => t.Id == id)
                                                       .SingleOrDefaultAsync();

                return new ServiceResult(car);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> SaveAsync(Car car)
        {
            try {
                await _dataContext.AddOrUpdateAsync(car);
                _dataContext.SaveChanges();

                return new ServiceResult(car);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }
    }
}
