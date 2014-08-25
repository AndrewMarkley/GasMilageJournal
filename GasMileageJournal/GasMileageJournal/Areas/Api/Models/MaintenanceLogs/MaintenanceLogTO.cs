using System;
using GasMileageJournal.Areas.Api.Models.Cars;
using GasMileageJournal.Models.MaintenanceLogs;

namespace GasMileageJournal.Areas.Api.Models.MaintenanceLogs
{
// ReSharper disable once InconsistentNaming
    public class MaintenanceLogTO
    {
        public String Id { get; set; }
        public DateTime CreatedOn { get; set; }
        public DateTime UpdatedOn { get; set; }
        public String CarId { get; set; }
        public Decimal Odometer { get; set; }
        public String Title { get; set; }
        public String Description { get; set; }
        public Decimal TotalCost { get; set; }
        public String Location { get; set; }
        public String Comments { get; set; }
        public DateTime? Date { get; set; }
        public Byte[] Receipt { get; set; }

        public static MaintenanceLogTO FromData(MaintenanceLog data)
        {
            var result = new MaintenanceLogTO
            {
                Id = data.Id,
                CreatedOn = data.CreatedOn,
                UpdatedOn = data.UpdatedOn,
                CarId = data.CarId,
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