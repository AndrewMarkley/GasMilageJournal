using System;
using System.Collections.Generic;
using GasMileageJournal.Areas.Api.Models.Cars;
using GasMileageJournal.Areas.Api.Models.FillUps;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.MaintenanceLogs;

namespace GasMileageJournal.Areas.Api.Models.MaintenanceLogs
{
// ReSharper disable once InconsistentNaming
    public class MaintenanceLogTO
    {
        public String Id { get; set; }
        public DateTime CreatedOn { get; set; }
        public DateTime UpdatedOn { get; set; }
        public virtual CarTO Car { get; set; }
        public Decimal Odometer { get; set; }
        public Decimal Title { get; set; }
        public Decimal Description { get; set; }
        public Decimal TotalCost { get; set; }
        public Decimal Location { get; set; }
        public Decimal Comments { get; set; }
        public DateTime Date { get; set; }
        public Byte[] Receipt { get; set; }

        public static MaintenanceLogTO FromData(MaintenanceLog data)
        {
            var result = new MaintenanceLogTO
            {
                Id = data.Id,
                CreatedOn = data.CreatedOn,
                UpdatedOn = data.UpdatedOn,
                Car = CarTO.FromData(data.Car),
                Odometer = data.Odometer,
                Title = data.Title,
                Description = data.Description,
                TotalCost = data.TotalCost,
                Location = data.Location,
                Comments = data.Comments,
                Date = data.Date,
                Receipt = data.Receipt
            };

            return result;
        }
    }
}