using System;
using GasMileageJournal.Areas.Api.Models.Cars;
using GasMileageJournal.Models.Cars;
using GasMileageJournal.Models.FillUps;

namespace GasMileageJournal.Areas.Api.Models.FillUps
{
// ReSharper disable once InconsistentNaming
    public class FillUpTO
    {
        public String Id { get; set; }
        public virtual String CarId { get; set; }
        public Decimal Distance { get; set; }
        public Decimal Gas { get; set; }
        public Decimal Price { get; set; }
        public Decimal TotalCost { get; set; }
        public Decimal Mpg { get; set; }
        public String Comments { get; set; }
        public DateTime? FillUpDate { get; set; }
        public DateTime CreatedOn { get; set; }
        public DateTime UpdatedOn { get; set; }
        public Byte[] Receipt { get; set; }

        public static FillUpTO FromData(FillUp data)
        {
            var result = new FillUpTO
            {
                CreatedOn = data.CreatedOn,
                UpdatedOn = data.UpdatedOn,
                Id = data.Id,
                CarId = data.CarId,
                Distance = data.Distance,
                Gas = data.Gas,
                Price = data.Price,
                TotalCost = data.TotalCost,
                Mpg = data.Mpg,
                Comments = data.Comments,
                FillUpDate = data.FillUpDate,
                Receipt = data.Receipt
            };

            return result;
        }
    }
}