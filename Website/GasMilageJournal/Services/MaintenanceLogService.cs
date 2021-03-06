﻿using GasMilageJournal.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using GasMilageJournal.Models;
using Microsoft.Data.Entity;
using GasMilageJournal.Extensions;

namespace GasMilageJournal.Services
{
    public class MaintenanceLogService : IMaintenanceLogService
    {
        private DataContext _dataContext;
        private IApplicationService _appService;

        public MaintenanceLogService(DataContext dataContext, IApplicationService appService)
        {
            _dataContext = dataContext;
            _appService = appService;
        }

        public async Task<ServiceResult> DeleteAsync(Guid id)
        {
            try {
                var maintenanceLog = await _dataContext.MaintenanceLogs.Where(t => t.Id == id)
                                                       .SingleOrDefaultAsync();

                await DeleteAsync(maintenanceLog);

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> DeleteAsync(MaintenanceLog maintenanceLog)
        {
            try {
                _dataContext.MaintenanceLogs.Remove(maintenanceLog);
                await _dataContext.SaveChangesAsync();

                return new ServiceResult();
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> Edit(string id)
        {
            try {
                MaintenanceLog maintenanceLog;

                if (id.ToLower().Equals("new")) {
                    maintenanceLog = new MaintenanceLog
                    {
                        Created = DateTime.Now,
                        UserId = _appService.UserId
                    };
                } else {
                    maintenanceLog = await _dataContext.MaintenanceLogs.Where(t => t.UserId == _appService.UserId)
                                                       .Where(t => t.Id == id.ToGuid())
                                                       .SingleAsync();
                }

                return new ServiceResult(maintenanceLog);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetAllAsync()
        {
            try {
                var maintenanceLogs = await _dataContext.MaintenanceLogs.ToListAsync();

                return new ServiceResult(maintenanceLogs);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> GetByIdAsync(Guid id)
        {
            try {
                var maintenanceLog = await _dataContext.MaintenanceLogs.Where(t => t.Id == id)
                                                       .SingleOrDefaultAsync();

                return new ServiceResult(maintenanceLog);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }

        public async Task<ServiceResult> SaveAsync(MaintenanceLog maintenanceLog)
        {
            try {
                await _dataContext.AddOrUpdateAsync(maintenanceLog);
                _dataContext.SaveChanges();

                return new ServiceResult(maintenanceLog);
            } catch (Exception ex) {
                return new ServiceResult(ex);
            }
        }
    }
}
