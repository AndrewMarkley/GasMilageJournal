using System;
using System.Collections.Generic;
using GasMileageJournal.Areas.Api.Models.FillUps;
using GasMileageJournal.Areas.Api.Models.MaintenanceLogs;
using GasMileageJournal.Models.Cars;

namespace GasMileageJournal.Areas.Api.Models.Cars
{
// ReSharper disable once InconsistentNaming
    public class CarTO
    {
        public String Id { get; set; }
        public DateTime CreatedOn { get; set; }
        public DateTime UpdatedOn { get; set; }
        public Int32 Year { get; set; }
        public String Name { get; set; }
        public String Make { get; set; }
        public String Model { get; set; }
        public Decimal Mileage { get; set; }
        public virtual List<MaintenanceLogTO> MaintenanceLogs { get; set; }
        public virtual List<FillUpTO> FillUps { get; set; }

        public static CarTO FromData(Car data)
        {
            var result = new CarTO
            {
                Id = data.Id,
                Name = data.Name,
                CreatedOn = data.CreatedOn,
                UpdatedOn = data.UpdatedOn,
                Year = data.Year,
                Make = data.Make,
                Model = data.Model,
                Mileage = data.Mileage,
                MaintenanceLogs = new List<MaintenanceLogTO>(),
                FillUps = new List<FillUpTO>(),
            };

            if (data.FillUps != null) {
                foreach (var value in data.FillUps) {
                    result.FillUps.Add(FillUpTO.FromData(value));
                }
            }

            if (data.MaintenanceLogs != null) {
                foreach (var value in data.MaintenanceLogs) {
                    result.MaintenanceLogs.Add(MaintenanceLogTO.FromData(value));
                }
            }

            return result;
        }
    }
}